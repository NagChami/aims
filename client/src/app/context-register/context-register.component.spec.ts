import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ContextRegisterComponent } from './context-register.component';

describe('ContextRegisterComponent', () => {
  let component: ContextRegisterComponent;
  let fixture: ComponentFixture<ContextRegisterComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ContextRegisterComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ContextRegisterComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
