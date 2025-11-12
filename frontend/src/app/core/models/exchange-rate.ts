import { CurrencyType } from './enum/currency-type';

export interface ExchangeRate {
  id: number;
  baseCurrency: CurrencyType;
  targetCurrency: CurrencyType;
  rate: number;
  effectiveDate: string;
  createdDate: string;
  lastModifiedDate: string;
  version: number;
  isActive: boolean;
}
