import {Component, OnInit} from '@angular/core';
import {BranchService} from '../../../services/branch/branch-service';
import {MatTableDataSource} from '@angular/material/table';
import {Branch} from '../../../models/branch';
import {Observable} from 'rxjs';
import {ActivatedRoute} from '@angular/router';

@Component({
  selector: 'app-branch-detail',
  imports: [],
  templateUrl: './branch-detail.html',
  styleUrl: './branch-detail.css'
})
export class BranchDetail implements OnInit {

/*  branch: Branch[];
  id: number;*/
  dataSource = new MatTableDataSource<Branch>([]);

  constructor(private branchService: BranchService, private route: ActivatedRoute) {
  }

  ngOnInit() {
   /* this.branchService.getBranch(this.branch.id).subscribe(branch => this.dataSource.data = branch);*/

  }
}
