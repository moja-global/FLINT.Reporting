export interface ChartConfig {
    id: number;
    title: string;
    subtitle: string;
    uri: string;
    dataLabelledLineChart: boolean;
    percentageAreaChart: boolean;
    basicColumnChart: boolean;
    stackedPercentageColumnChart: boolean;
    selectedChart: string;
    chartXAxisLabel: string;
    chartYAxisLabel: string;
    units: string;
    showSeriesLabelsInLegend: boolean;
}
