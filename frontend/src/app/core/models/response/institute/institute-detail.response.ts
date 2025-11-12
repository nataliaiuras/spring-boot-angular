import {BranchResponse} from '../branch/branch-response';
import {InstituteResponse} from './institute.response';

export interface InstituteDetailResponse extends InstituteResponse {
  branches: BranchResponse[];
  createdDate: string;
  version: number;
}
