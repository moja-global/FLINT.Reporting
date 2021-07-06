export class Database {

	[key: string]: string | number | boolean | undefined | null;
	pos: number | undefined | null;
	id: number | undefined | null;
	label: string | undefined | null;
	description: string | undefined | null;
	url: string | undefined | null;	
	startYear: number | undefined | null;
	endYear: number | undefined | null;
	processed: boolean = false;
	published: boolean = false;
	archived: boolean =  false
	version: number | undefined | null;

    constructor(options?: Partial<Database>) {
        Object.assign(this, options);
    }	
}
