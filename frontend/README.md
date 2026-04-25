# Digital Gold Wallet – Angular Frontend

A complete evaluator dashboard frontend for the **Digital Gold Wallet** Spring Boot backend.

## Tech Stack
- **Angular 18** (Standalone components, lazy loading)
- **Tailwind CSS** (Dark theme UI)
- **Angular Router** (Protected routes)
- **Reactive Forms** + FormsModule
- **HttpClient** with auth interceptor

---

## How to Run

```bash
# 1. Install dependencies
npm install

# 2. Start the dev server
npm start
# → http://localhost:4200
```

Your Spring Boot backend should be running at `http://localhost:8080`.

---

## Demo Login Credentials

| Username     | Password      | Access              |
|--------------|---------------|---------------------|
| `admin`      | `admin123`    | All modules         |
| `pavithra`   | `pavithra123` | User & Address      |
| `caitlyn`    | `caitlyn123`  | Vendor & Branch     |
| `suba`       | `suba123`     | Gold (Virtual & Physical) |
| `bavanagaran`| `bavan123`    | Payment & Wallet    |
| `mirudhula`  | `mirudhula123`| Transaction         |

---

## Project Structure

```
src/app/
  core/
    auth/          ← AuthService, Guards, Interceptor, MockUsers
    services/      ← ApiService, NotificationService
    models/        ← ApiResponse, AuthUser interfaces
  shared/
    components/    ← Navbar, PageHeader, ResultViewer, DeveloperCard
    constants/     ← developers.constant.ts (all endpoint metadata)
  features/
    auth/          ← Login page
    home/          ← Dashboard with all developer cards
    endpoint-explorer/ ← Grouped endpoint browser
    user/          ← Pavithra's module (8 endpoints)
    vendor/        ← Caitlyn's module (9 endpoints)
    gold/          ← Suba's module (6 endpoints)
    payment/       ← Bavanagaran's payment (3 endpoints)
    wallet/        ← Bavanagaran's wallet (3 endpoints)
    transaction/   ← Mirudhula's module (3 endpoints)
```

---

## Replacing Mock Auth with Real Spring Security

When your backend implements Spring Security + JWT:

**Step 1** – Update `AuthService` (`src/app/core/auth/auth.service.ts`):
```typescript
// Replace the mock login logic with:
login(req: LoginRequest): Observable<AuthUser> {
  return this.http.post<{token: string}>(`${environment.apiBaseUrl}/api/v1/auth/login`, req).pipe(
    map(res => {
      const decoded = jwtDecode(res.token); // use jwt-decode library
      const user: AuthUser = {
        username: decoded.sub,
        displayName: decoded.name,
        role: decoded.role,
        modules: decoded.modules,
        token: res.token
      };
      localStorage.setItem('dgw_user', JSON.stringify(user));
      this.currentUserSignal.set(user);
      return user;
    })
  );
}
```

**Step 2** – The `authInterceptor` already attaches `Bearer <token>` – no changes needed.

**Step 3** – The `authGuard` and `roleGuard` already use `AuthService` – no changes needed.

---

## Developer Module Summary

| # | Developer     | Module(s)         | Endpoints |
|---|---------------|-------------------|-----------|
| 1 | Pavithra      | User, Address     | 8         |
| 2 | Caitlyn Mary  | Vendor, Branch    | 9         |
| 3 | Suba Harini   | Gold (V + P)      | 6         |
| 4 | Bavanagaran   | Payment, Wallet   | 6         |
| 5 | Mirudhula     | Transaction       | 3         |
|   |               | **Total**         | **32**    |
