import { TestSuite } from "./TestSuite.model";

export class Exigences {
    id!: number;
    idExigence!: string;
    titre!: string;
    description!: string;
    commentaire!: string;
    completed: boolean = false;

    // Relation avec TestSuite - une mission peut avoir plusieurs tests
    testSuites?: TestSuite[];
}
