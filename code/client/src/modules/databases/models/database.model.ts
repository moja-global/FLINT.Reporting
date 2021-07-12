export class Database {

	[key: string]: string | number | boolean ;
	pos!:  number;
	id!:  number;
	label!: string;
	description!: string;
	url!: string;	
	startYear!:  number;
	endYear!:  number;
	processed: boolean = false;
	published: boolean = false;
	archived: boolean =  false
	version!:  number;

    constructor(options?: Partial<Database>) {
        Object.assign(this, options);
    }	
}
