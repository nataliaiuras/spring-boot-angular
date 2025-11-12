import {InstituteResponse} from '../institute/institute.response';
import {AddressResponse} from '../address/address-response';
import {CustomerResponse} from '../customer/customer-response';
import {BranchResponse} from './branch-response';

export interface BranchDetailResponse extends BranchResponse {

  address: AddressResponse;
  institute: InstituteResponse;
  customers: CustomerResponse[];
  createdDate: string;
  version: number;

}
