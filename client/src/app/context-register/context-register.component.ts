import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';

import * as L from 'leaflet';

// Fix default marker icon paths for Angular builds
delete (L.Icon.Default.prototype as any)._getIconUrl;
L.Icon.Default.mergeOptions({
  iconRetinaUrl: 'assets/leaflet/marker-icon-2x.png',
  iconUrl: 'assets/leaflet/marker-icon.png',
  shadowUrl: 'assets/leaflet/marker-shadow.png',
});




type TabKey = 'role' | 'parties' | 'needs' | 'issues';
type ValueDisplayType =
  | 'TEXT'
  | 'TEXTAREA'
  | 'NUMBER'
  | 'DATE'
  | 'DATETIME'
  | 'BOOLEAN'
  | 'JSON'
  | 'SELECT'
  | 'MAP';

type RoleContextRow = {
  id?: number;
  aspect: string;
  description: string;
};

type RoleSection = {
  id: number;
  aspectName: string;
  displayOrder?: number;
  children: RoleItem[];
};
type RoleItem = {
  id: number;
  aspectName: string;
  descriptionText: string | null;
  displayOrder?: number;

  valueDisplayType?: ValueDisplayType | null;
  // optional later
    options?: Array<{ label: string; value: string }>;
};



@Component({
  selector: 'app-context-register',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './context-register.component.html',
  styleUrls: ['./context-register.component.css'],
})
export class ContextRegisterComponent {

  activeTab: TabKey = 'role';
  search = '';
  // change this to the correct register id for now
  contextRegisterId = 1;

  loading = false;
  errorMsg = '';

  saveMsg = '';
  roleSections: RoleSection[] = [];

  // Snapshot for Cancel + change detection
  private originalJson = '';

  // UI state
  isDirty = false;
  saving = false;

  // UI scaffold data (later we’ll load from API)
  roleRows: RoleContextRow[] = [
    { aspect: 'Nature of Business', description: '...' },
    { aspect: 'Mission', description: '...' },
    { aspect: 'Vision', description: '...' },
  ];

  mapOpen = false;
  mapQuery = '';
  private map?: L.Map;
  private marker?: L.Marker;
  private activeMapItem: any | null = null;
  private mapTarget: 'ITEM' | 'ADD' = 'ITEM';
  private onMapPicked: ((pickedValue: string) => void) | null = null;





  openMapForAdd() {
    this.mapTarget = 'ADD';


     // when user picks a place, write it to addForm.value
      this.onMapPicked = (picked) => {
        this.addForm.value = picked;
      };

      const raw = (this.addForm.value || '').trim() || 'Pick a location';
      this.openMapRaw(raw);
  }

  private openMapRaw(raw: string) {


     this.mapQuery = raw?.trim() || 'Pick a location';
      this.mapOpen = true;
      setTimeout(() => this.initMapAndLocate(this.mapQuery), 0);


  }



  /*openMap(item: any) {
      this.activeMapItem = item;
      this.mapQuery = (item?.descriptionText || '').trim() || 'Pick a location';
      this.mapOpen = true;
      setTimeout(() => this.initMapAndLocate(this.mapQuery), 0);
  }*/

  openMap(item: any) {
    this.mapTarget = 'ITEM';
    this.activeMapItem = item;

    this.onMapPicked = (picked) => {
        item.descriptionText = picked;
        this.markDirty();
      };

      const raw = (item?.descriptionText || '').trim() || 'Pick a location';
      this.openMapRaw(raw);
  }


  closeMap() {
    this.mapOpen = false;
    if (this.map) {
      this.map.remove();
      this.map = undefined;
      this.marker = undefined;
    }
      // do not wipe add form; only clear item pointer
      this.activeMapItem = null;



  }




  private async initMapAndLocate(query: string) {
    this.map = L.map('leafletMap', { zoomControl: true }).setView([20, 0], 2);

    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
      attribution: '&copy; OpenStreetMap contributors'
    }).addTo(this.map);

    //const primary = query.split(',')[0]?.trim() || query;
    //const primary = query.trim();
    const q = this.buildGeocodeQuery(query);
    if (!q) return;

    const url = `https://nominatim.openstreetmap.org/search?format=json&addressdetails=1&limit=1&q=${encodeURIComponent(q)}`;



   // const url = `https://nominatim.openstreetmap.org/search?format=json&q=${encodeURIComponent(primary)}`;
    const res = await fetch(url, { headers: { 'Accept': 'application/json' } });
    const data = await res.json();

    if (!data?.length) return;

    const lat = parseFloat(data[0].lat);
    const lon = parseFloat(data[0].lon);

    this.marker = L.marker([lat, lon]).addTo(this.map);
    this.map.setView([lat, lon], 5);
    L.circle([lat, lon], { radius: 250000 }).addTo(this.map);



    // Click-to-pick: update value from clicked map point and close
  this.map.on('click', async (ev: L.LeafletMouseEvent) => {
    const lat = ev.latlng.lat;
    const lon = ev.latlng.lng;

    if (this.marker) this.marker.remove();
    this.marker = L.marker([lat, lon]).addTo(this.map!);

    const picked = await this.reverseGeocode(lat, lon);

    // ✅ write back to whoever opened the map
    if (this.onMapPicked) {
      this.onMapPicked(picked);
    }

    this.closeMap();
  });


  }

  constructor(private http: HttpClient) {}

  ngOnInit(): void {
    console.log('In ngOnInit----')
    this.loadRoleAndContext();
  }

  setTab(tab: TabKey) {
    this.activeTab = tab;

    // load only when needed (optional)
    if (tab === 'role' && this.roleRows.length === 0) {
          this.loadRoleAndContext();
    }
  }

  addRoleRow() {
    this.roleRows = [{ aspect: '', description: '' }, ...this.roleRows];
  }

  loadRoleAndContext() {
      this.loading = true;
      this.errorMsg = '';
      this.saveMsg = '';
          this.isDirty = false;

      const url = `/aims/api/contextRegister?contextRegisterId=1`;

      this.http.get<any>(url).subscribe({
        next: (res) => {
          this.roleSections = (res?.roleAndContext ?? []) as RoleSection[];

          // store snapshot for cancel + dirty tracking
          this.originalJson = this.serializeForCompare(this.roleSections);
          this.isDirty = false;

          this.loading = false;
        },
        error: (err) => {
          console.error(err);
          this.errorMsg = 'Failed to load Role and Context.';
          this.loading = false;
        }
      });
  }

  get filteredRoleSections(): RoleSection[] {
      const q = this.search.trim().toLowerCase();
      if (!q) return this.roleSections;

      // filter items inside each section; hide empty sections
      return this.roleSections
        .map(sec => ({
          ...sec,
          children: (sec.children ?? []).filter(ch =>
            (ch.aspectName || '').toLowerCase().includes(q) ||
            ((ch.descriptionText ?? '')).toLowerCase().includes(q)
          )
        }))
        .filter(sec => sec.children.length > 0 || (sec.aspectName || '').toLowerCase().includes(q));
  }

  private async reverseGeocode(lat: number, lon: number): Promise<string> {
    const url = `https://nominatim.openstreetmap.org/reverse?format=json&addressdetails=1&lat=${lat}&lon=${lon}`;
      const res = await fetch(url, { headers: { 'Accept': 'application/json' } });
      const data = await res.json();
      const a = data?.address || {};

      const city = a.city || a.town || a.village || a.municipality || '';
      const state = a.state || '';
      const country = a.country || '';

      // Prefer: "Madurai, Tamil Nadu, India"
      const parts = [city, state, country].filter(Boolean);
      return parts.length ? parts.join(', ') : (data?.display_name || `${lat.toFixed(4)}, ${lon.toFixed(4)}`);
  }

  private buildGeocodeQuery(input: string): string {
    const raw = (input || '').trim();
    if (!raw) return '';

    const parts = raw.split(',').map(p => p.trim()).filter(Boolean);
    if (parts.length <= 1) return raw;

    const last = parts[parts.length - 1].toLowerCase();

    // if last token is a broad region keyword, ignore it and geocode the first token
    const broadRegion =
      last.includes('asia') ||
      last.includes('africa') ||
      last.includes('europe') ||
      last.includes('america') ||
      last.includes('oceania') ||
      last.includes('middle east');

    if (broadRegion) return parts[0]; // "India" from "India, South Asia"

    // Otherwise geocode full (best for "Madurai, Tamil Nadu, India")
    return raw;
  }

  markDirty() {
      const now = this.serializeForCompare(this.roleSections);
      this.isDirty = now !== this.originalJson;
  }

  cancelAll() {
    if (!this.originalJson) return;
    // restore snapshot
    this.roleSections = JSON.parse(this.originalJson) as RoleSection[];
    this.isDirty = false;
    this.saveMsg = 'Changes discarded.';
  }

  // UI-only for now. We will hook backend after you confirm UI.
  saveAll() {
    this.saveMsg = '';
    this.saving = true;

    const payload = this.buildSavePayload();
    console.log('SAVE PAYLOAD (UI only for now):', payload);

    // For now just simulate success
    setTimeout(() => {
      this.originalJson = this.serializeForCompare(this.roleSections);
      this.isDirty = false;
      this.saving = false;
      this.saveMsg = 'Ready to save (backend wiring next).';
    }, 300);
  }

private buildSavePayload() {
    // Flatten Role & Context items (each has its own id)
    const roleItems = (this.roleSections ?? [])
      .flatMap(sec => (sec.children ?? []).map(ch => ({
        id: ch.id,
        aspectName: ch.aspectName,
        descriptionText: ch.descriptionText,
        valueDisplayType: ch.valueDisplayType ?? 'TEXT',
        parentId: sec.id,
        displayOrder: ch.displayOrder ?? null
      })));

    return {
      contextRegisterId: this.contextRegisterId,
      roleAndContext: roleItems
      // later: interestedParties, needsExpectations, issues...
    };
  }

  private serializeForCompare(obj: any): string {
    // stable-ish compare: stringify without functions
    return JSON.stringify(obj);
  }

addOpen = false;

addForm = {
  parentSectionId: 0,
  aspectName: '',
  valueDisplayType: 'TEXT' as ValueDisplayType,
  value: '' as string,
};

get roleSectionOptions() {
  return (this.roleSections ?? []).map(s => ({ id: s.id, name: s.aspectName }));
}

openAddRole() {
  // default to first section if exists
  const firstId = this.roleSections?.[0]?.id ?? 0;

  this.addForm = {
    parentSectionId: firstId,
    aspectName: '',
    valueDisplayType: 'TEXT',
    value: '',
  };

  this.addOpen = true;
}

cancelAdd() {
  this.addOpen = false;
}

saveAdd() {
  // UI-only insert (backend later)
  const sec = this.roleSections.find(s => s.id === this.addForm.parentSectionId);
  if (!sec) return;

  // temp client-side id (negative) until DB gives real id
  const tempId = -Math.floor(Math.random() * 1000000);

  sec.children = [
    {
      id: tempId,
      aspectName: this.addForm.aspectName.trim(),
      descriptionText: this.addForm.value,
      valueDisplayType: this.addForm.valueDisplayType,
      parentId: sec.id,
      displayOrder: (sec.children?.length ?? 0) + 1
    } as any,
    ...(sec.children ?? [])
  ];

  this.addOpen = false;
  this.markDirty();
}



}
