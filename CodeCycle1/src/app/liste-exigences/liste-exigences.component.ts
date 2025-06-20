// import {Component, inject, Input, Output, EventEmitter } from '@angular/core';
// import {FormBuilder, Validators, FormsModule, ReactiveFormsModule} from '@angular/forms';
// import {STEPPER_GLOBAL_OPTIONS} from '@angular/cdk/stepper';
// import {MatButtonModule} from '@angular/material/button';
// import {MatInputModule} from '@angular/material/input';
// import {MatFormFieldModule} from '@angular/material/form-field';
// import {MatStepperModule} from '@angular/material/stepper';
// import { Exigences } from '../model/exigence.model';
// import { UserService } from '../service/user.service';
// import { User } from '../model/user.model';


// @Component({
//   selector: 'app-liste-exigences',
//   standalone: true,                    
//   imports: [
//     FormsModule,
//     ReactiveFormsModule,
//     MatStepperModule,
//     MatFormFieldModule,
//     MatInputModule,
//     MatButtonModule
//   ],
//   templateUrl: './liste-exigences.component.html',
//   styleUrls: ['./liste-exigences.component.css'],
//   providers: [
//     { provide: STEPPER_GLOBAL_OPTIONS, useValue: { showError: true } }
//   ]
// })
// export class ListeExigencesComponent{
// @Input() user! : User;
// @Output() transmettre = new EventEmitter<User>();

//   onTransmettre() {
//     (document.activeElement as HTMLElement)?.blur();
//     this.transmettre.emit(this.user);
//   }



//   private _formBuilder = inject(FormBuilder);

//   firstFormGroup = this._formBuilder.group({
//     firstCtrl: ['', Validators.required],
//   });
//   secondFormGroup = this._formBuilder.group({
//     secondCtrl: ['', Validators.required],
//   });
//   thirdFormGroup = this._formBuilder.group({
//     thirdCtrl: ['', Validators.required],
//   });
//   fourthFormGroup = this._formBuilder.group({
//     fourthCtrl: [''],
//   });

//   newMission = new Exigences();


//   constructor (private userService: UserService){
//   }

  

//   ajouterMission():string{ 
    
//     this.userService.createExigence(this.newMission).subscribe( mission => {
//             //this.newMission = mission ;
//             this.newMission = mission ;
//             console.log(this.newMission);
            
//     });
//     return this.newMission.idExigence;
//   }


// }


