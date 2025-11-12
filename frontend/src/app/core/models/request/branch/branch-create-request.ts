import {AddressCreateRequest} from '../address/address-create-request';

export interface BranchCreateRequest {

  branchCode: string;
  locationCode: string;
  name: string;
  email: string;
  address: AddressCreateRequest;
  phoneNumber: string;
  instituteId: number;

}
