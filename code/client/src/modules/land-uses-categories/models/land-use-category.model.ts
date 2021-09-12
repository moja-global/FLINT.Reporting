export class LandUseCategory {

	[key: string]: string | number | null;
	pos!: number | null;
	id!: number | null;
	reportingFrameworkId!: number | null;
	parentLandUseCategoryId!: number | null;
	parentLandUseCategoryName!: string | null;
	coverTypeId!: number | null;
	name!: string | null;
	version!: number | null;

    constructor(options?: Partial<LandUseCategory>) {
        Object.assign(this, options);
    }	
}