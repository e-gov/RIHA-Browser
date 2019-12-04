import {Injectable} from '@angular/core';
import {ActivatedRouteSnapshot, CanDeactivate, Router, RouterStateSnapshot} from '@angular/router';
import {Location} from '@angular/common';
import {Observable} from 'rxjs';

export interface CanDeactivateModal {
  canDeactivate: () => Observable<boolean> | Promise<boolean> | boolean;
}

@Injectable({
  providedIn: 'root'
})

export class CanDeactivateModalGuard implements CanDeactivate<CanDeactivateModal> {

  constructor(
    private location: Location,
    private router: Router
  ) { }

  canDeactivate(component: CanDeactivateModal, currentRoute: ActivatedRouteSnapshot,
                currentState: RouterStateSnapshot, nextState: RouterStateSnapshot) {
    let canDeactivate: Observable<boolean> | Promise<boolean> | boolean = true;
    if (component.canDeactivate) {
      canDeactivate = component.canDeactivate();
      if (canDeactivate instanceof Observable) {
        canDeactivate.subscribe(answer => {
          return this.canDeactivateCallback(answer, currentState);
        })
      } else if (canDeactivate instanceof Promise) {
        (<Promise<boolean>>canDeactivate).then(answer => {
          return this.canDeactivateCallback(answer, currentState);
        })
      }

      return canDeactivate;
    }

    return canDeactivate;
  }

  // component canDeactivate Promise | Observable callback handler
  canDeactivateCallback(answer, currentState: RouterStateSnapshot) {
    // this trick is used 'cause of angular router bug in canDeactivate guard
    // see Deactivation Guard breaks the routing history #13586 (https://github.com/angular/angular/issues/13586)
    if (!answer && this.router.getCurrentNavigation().trigger === 'popstate') {
      this.location.go(currentState.url);
    }

    return answer;
  }
}
