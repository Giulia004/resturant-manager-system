import { inject } from "@angular/core";
import { CanActivateFn, Router } from "@angular/router";
import { AuthService } from "../services/auth.service";
import { Ruolo } from "../services/user.service";

export const roleGuard: CanActivateFn = (route) => {
    const authService = inject(AuthService);
    const router = inject(Router);

    if (!authService.isLoggedIn()) {
        router.navigate(['/login'])
        return false;
    }

    const effectiveRoles = route.data['roles'] as Ruolo[] | undefined;

    if (!effectiveRoles || effectiveRoles.length === 0) return true;

    const userRole = authService.getRole();

    if (userRole && effectiveRoles.includes(userRole)) return true;

    router.navigate(['/dashboard']);
    return false;
};