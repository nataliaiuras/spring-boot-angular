import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BranchTable } from './branch-table';

describe('BranchTable', () => {
  let component: BranchTable;
  let fixture: ComponentFixture<BranchTable>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [BranchTable]
    })
    .compileComponents();

    fixture = TestBed.createComponent(BranchTable);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
