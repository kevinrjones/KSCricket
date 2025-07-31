import { definePreset } from '@primeng/themes';
import Aura from '@primeng/themes/aura';

// --bg-brand-50: #f5f5f5;
// --bg-brand-100: #addf97;
// --bg-brand-200: #a9c52f;
// --bg-brand-300: #6ea474;
// --bg-brand-400: #21612A;
// --bg-brand-500: #21612A;
// /* ACS Color */
// --bg-brand-700: #346b3c;
// --bg-brand-800: #295542;
// --bg-brand-900: #194920;

export const AcsPreset = definePreset(Aura, {
  semantic: {
    primary: {
      50: '#f5f5f5',
      100: '#addf97',
      200: '#a9c52f',
      300: '#6ea474',
      400: '#21612A',
      500: '#21612A',
      600: '#346b3c',
      700: '#346b3c',
      800: '#295542',
      900: '#194920',
      950: '#194920'
    }
  }
});

// export const AcsPreset = AcsPreset;
