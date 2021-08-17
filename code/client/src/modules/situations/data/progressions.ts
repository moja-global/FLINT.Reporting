import { Progression } from '../models';

export const PROGRESSIONS: Progression[] = [
    {
        id: 1,
        title: "Epidemiological Curve for COVID-19 Outbreak",
        statusIndicators: [11,13,15],
        statusIndicatorsGenericTitle: "# of cumulative cases",
        paceIndicators: [10,12,14],
        paceIndicatorsGenericTitle: "# of daily new cases",
        graphGenerationSteps: 5
    }
];
