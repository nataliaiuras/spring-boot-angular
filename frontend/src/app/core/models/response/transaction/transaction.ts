import { AccountResponse } from '../account/account-response';
import { CurrencyType } from '../../enum/currency-type';
import { OperationType } from '../../enum/operation-type';
import { TransactionStatus } from '../../enum/transaction-status';

export interface Transaction {
  id: number;
  operationType: OperationType;
  sourceAccount: AccountResponse;
  destinationAccount: AccountResponse;
  sourceAmount: number;
  sourceCurrency: CurrencyType;
  destinationCurrency: CurrencyType;
  conversionRate: number;
  conversionFee: number;
  convertedAmount: number;
  operationFee: number;
  destinationAmount: number;
  referenceNumber: string;
  description: string;
  status: TransactionStatus;
  transactionDate: string;
  createdDate: string;
  lastModifiedDate: string;
  version: number;
}
