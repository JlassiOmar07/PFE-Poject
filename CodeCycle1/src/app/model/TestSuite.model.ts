import { User } from "./user.model";
import { Exigences } from "./exigence.model";

export class TestSuite {
    id!: number;
    name !: string;
    tag!: string;
    executionDate! : String;
    executionTimeMs!: number;
    totalTests!: number;
    passedTests! :number;
    failedTests!:number;
    skippedTests!:number;
    successRate!: number;
    logs!: string;
    jsonReport!: string;
    user! : User;

    // Relation avec Mission - un test est lié à une mission complète
    mission?: Exigences;
}