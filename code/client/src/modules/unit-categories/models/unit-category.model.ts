export class UnitCategory {

	[key: string]: string | number | undefined | null;
	pos: number | undefined | null;
	id: number | undefined | null;
	name: string | undefined | null;
	version: number | undefined | null;

    constructor(options?: Partial<UnitCategory>) {
        Object.assign(this, options);
    }	
}