# Store Manager API

Backend API untuk mengelola **Toko, Cabang, dan Provinsi** dengan fitur **autentikasi JWT**.

---

## Fitur
- **Autentikasi** (Login / Register / Logout)
- **Manajemen Provinsi** 
- **Manajemen Cabang**
- **Manajemen Toko**
- **Pencarian toko berdasarkan nama provinsi**
- **Whitelist Stores** 
- **Hanya menampilkan data aktif** 
- **Data dummy otomatis**: 34 provinsi, ±100 cabang, ±20.000 toko

---

## Teknologi
- **Java 24** + **Spring Boot 3.5.5**
- **MySQL Database**
- **JWT** untuk autentikasi
- **Spring Security** untuk otorisasi
- **Hibernate JPA** untuk ORM
- **Maven** untuk dependency management

---

## Prerequisites
Pastikan sudah menginstall:
- **Java 24**
- **MySQL Server**
- **Maven**
- **Postman** (untuk uji coba API)

---

## Cara Menjalankan

### 1. Setup Database
```sql
-- Buat database
CREATE DATABASE store_manager;
```

### 2. Konfigurasi Database
Edit file `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/store_manager
spring.datasource.username=root
spring.datasource.password=password
```

### 3. Build & Run Aplikasi
```bash
# Build project
mvn clean compile

# Jalankan aplikasi
mvn spring-boot:run
```
Aplikasi berjalan di: **http://localhost:8080**

---

## Data Dummy Otomatis
Saat pertama kali dijalankan, aplikasi akan membuat:
- 34 provinsi di Indonesia
- ±100 cabang
- ±20.000 toko
- ±1.000 whitelist stores (5% dari total toko)

---

## Autentikasi

### Register User Baru
**Endpoint:** `POST /api/auth/register`  
**Body:**
```json
{
  "username": "testuser",
  "email": "test@example.com",
  "password": "password123",
  "fullName": "Test User"
}
```

### Login
**Endpoint:** `POST /api/auth/login`  
**Body:**
```json
{
  "username": "admin",
  "password": "admin123"
}
```

**Response:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "username": "admin",
  "fullName": "Administrator",
  "email": "admin@storemanager.com",
  "message": "Login successful"
}
```

### Logout
**Endpoint:** `POST /api/auth/logout`  
**Headers:**
```
Authorization: Bearer <your_token>
```

---

## API Endpoints

### Authentication
- `POST /api/auth/register` → Register user baru
- `POST /api/auth/login` → Login
- `POST /api/auth/logout` → Logout
- `GET /api/auth/validate` → Validasi token

### Provinces
- `GET /api/provinces` → Get semua provinsi
- `GET /api/provinces/{id}` → Get provinsi by ID
- `GET /api/provinces/name/{name}` → Get provinsi by name
- `POST /api/provinces` → Create provinsi baru
- `PUT /api/provinces/{id}` → Update provinsi
- `DELETE /api/provinces/{id}` → Soft delete provinsi

### Branches
- `GET /api/branches` → Get semua cabang aktif
- `GET /api/branches/{id}` → Get cabang by ID
- `POST /api/branches` → Create cabang baru
- `PUT /api/branches/{id}` → Update cabang
- `DELETE /api/branches/{id}` → Soft delete cabang

### Stores
- `GET /api/stores` → Get semua toko aktif
- `GET /api/stores/{id}` → Get toko by ID
- `GET /api/stores/province/{provinceName}` → Cari toko by nama provinsi
- `GET /api/stores/whitelisted` → Get semua whitelist stores
- `POST /api/stores` → Create toko baru
- `PUT /api/stores/{id}` → Update toko
- `DELETE /api/stores/{id}` → Soft delete toko

### Whitelist Stores
- `GET /api/whitelist-stores` → Get semua whitelist entries
- `POST /api/whitelist-stores/{storeId}` → Tambah toko ke whitelist
- `DELETE /api/whitelist-stores/{id}` → Hapus toko dari whitelist