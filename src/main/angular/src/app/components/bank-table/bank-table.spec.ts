import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BankTable } from './bank-table';

describe('BankTable', () => {
  let component: BankTable;
  let fixture: ComponentFixture<BankTable>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [BankTable]
    })
    .compileComponents();

    fixture = TestBed.createComponent(BankTable);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
