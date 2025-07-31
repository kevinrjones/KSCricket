export interface Ground {
  id: number,
  matchType: string,
  code: string,
  countryName: string,
  groundId: number,
  knownAs: string
}

export function defaultGround() {
  return {id: 0, matchType: '', code: '', countryName: '', groundId: 0, knownAs: 'All Grounds'}
}
