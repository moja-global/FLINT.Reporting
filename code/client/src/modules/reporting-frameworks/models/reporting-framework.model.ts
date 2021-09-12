export class ReportingFramework {

	[key: string]: string | number | undefined | null;
	pos: number | undefined | null;
	id: number | undefined | null;
	name: string | undefined | null;	
	description: string | undefined | null;
	version: number | undefined | null;

    constructor(options?: Partial<ReportingFramework>) {
        Object.assign(this, options);
    }	
}