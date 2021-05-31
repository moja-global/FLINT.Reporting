export class ReportingVariable {

	[key: string]: string | number | undefined | null;
	pos: number | undefined | null;
	id: number | undefined | null;
	reportingFrameworkId: number | undefined | null;
	name: string | undefined | null;
	description: string | undefined | null;
	version: number | undefined | null;

    constructor(options?: Partial<ReportingVariable>) {
        Object.assign(this, options);
    }

}
