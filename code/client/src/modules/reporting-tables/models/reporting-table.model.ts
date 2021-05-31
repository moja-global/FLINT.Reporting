export class ReportingTable {

	[key: string]: string | number | undefined | null;
	pos: number | undefined | null;
	id: number | undefined | null;
	reportingFrameworkId: number | undefined | null;
	number: string | undefined | null;
	name: string | undefined | null;
	description: string | undefined | null;
	version: number | undefined | null;

    constructor(options?: Partial<ReportingTable>) {
        Object.assign(this, options);
    }

}
