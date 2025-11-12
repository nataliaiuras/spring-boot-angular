import {Component, OnInit} from '@angular/core';
import {BranchService} from '../../../services/branch/branch-service';
import {MatTableDataSource} from '@angular/material/table';
import {BranchResponse} from '../../../core/models/response/branch/branch-response';
import {Observable} from 'rxjs';
import {ActivatedRoute} from '@angular/router';

@Component({
  selector: 'app-branch-detail',
  imports: [],
  templateUrl: './branch-detail-component.html',
  styleUrl: './branch-detail-component.css'
})
export class BranchDetailComponent implements OnInit {

/*  branch: Branch[];
  id: number;*/
  dataSource = new MatTableDataSource<BranchResponse>([]);

  constructor(private branchService: BranchService, private route: ActivatedRoute) {
  }

  ngOnInit() {
   /* this.branchService.getBranch(this.branch.id).subscribe(branch => this.dataSource.data = branch);*/

  }
}
