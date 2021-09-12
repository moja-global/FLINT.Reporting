export class CoverType {

	[key: string]: string | number | undefined | null;
	pos: number | undefined | null;
	id: number | undefined | null;
	code: string | undefined | null;
	description: string | undefined | null;
	version: number | undefined | null;

    constructor(options?: Partial<CoverType>) {
        Object.assign(this, options);
    }	
}