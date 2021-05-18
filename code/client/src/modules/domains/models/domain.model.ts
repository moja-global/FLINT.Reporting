export class Domain {

	[key: string]: string | number | undefined | null;
	pos: number | undefined | null;
	id: number | undefined | null;
	name: string | undefined | null;
	description: string | undefined | null;
	indicators: string | undefined | null;
	metadata: string | undefined | null;
	version: number | undefined | null;

    constructor(options?: Partial<Domain>) {
        Object.assign(this, options);
    }	
}