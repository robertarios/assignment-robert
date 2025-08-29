package com.company.storemanager.service;

import com.company.storemanager.model.Branch;
import com.company.storemanager.model.Province;
import com.company.storemanager.model.Store;
import com.company.storemanager.model.WhitelistStore;
import com.company.storemanager.repository.BranchRepository;
import com.company.storemanager.repository.ProvinceRepository;
import com.company.storemanager.repository.StoreRepository;
import com.company.storemanager.repository.WhitelistStoreRepository;
import com.github.javafaker.Faker;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class DataFakerService {

    @Autowired
    private ProvinceRepository provinceRepository;

    @Autowired
    private BranchRepository branchRepository;

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private WhitelistStoreRepository whitelistStoreRepository;

    private final Faker faker = new Faker(new Locale("id-ID"));
    private final Set<String> usedBranchCodes = new HashSet<>();
    private final Set<String> usedStoreCodes = new HashSet<>();

    @PostConstruct
    public void init() {
        if (provinceRepository.count() == 0) {
            createFakeData();
        }
    }

    @Transactional
    public void createFakeData() {
        try {
            System.out.println("Starting fake data creation...");
            
            // Create provinces (all provinces in Indonesia)
            List<Province> provinces = createProvinces();
            provinceRepository.saveAll(provinces);
            System.out.println("Created " + provinces.size() + " provinces");

            // Create branches (100 branches distributed across provinces)
            List<Branch> branches = createBranches(provinces);
            branchRepository.saveAll(branches);
            System.out.println("Created " + branches.size() + " branches");

            // Create stores (20,000 stores distributed across branches)
            createStoresInBatches(branches);

            System.out.println("Fake data created successfully!");
        } catch (Exception e) {
            System.err.println("Error creating fake data: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private List<Province> createProvinces() {
        List<Province> provinces = new ArrayList<>();
        String[] provinceNames = {
            "Aceh", "Sumatera Utara", "Sumatera Barat", "Riau", "Jambi",
            "Sumatera Selatan", "Bengkulu", "Lampung", "Kepulauan Bangka Belitung",
            "Kepulauan Riau", "DKI Jakarta", "Jawa Barat", "Jawa Tengah",
            "DI Yogyakarta", "Jawa Timur", "Banten", "Bali", "Nusa Tenggara Barat",
            "Nusa Tenggara Timur", "Kalimantan Barat", "Kalimantan Tengah",
            "Kalimantan Selatan", "Kalimantan Timur", "Kalimantan Utara",
            "Sulawesi Utara", "Sulawesi Tengah", "Sulawesi Selatan", "Sulawesi Tenggara",
            "Gorontalo", "Sulawesi Barat", "Maluku", "Maluku Utara", "Papua Barat", "Papua"
        };

        for (String name : provinceNames) {
            Province province = new Province();
            province.setName(name);
            province.setIsActive(true);
            provinces.add(province);
        }
        return provinces;
    }

    private List<Branch> createBranches(List<Province> provinces) {
        List<Branch> branches = new ArrayList<>();
        usedBranchCodes.clear();
        
        // Create 2-4 branches per province
        for (Province province : provinces) {
            int branchesCount = faker.random().nextInt(2, 5);
            String provinceAbbrev = getProvinceAbbreviation(province.getName());
            
            for (int i = 1; i <= branchesCount; i++) {
                Branch branch = new Branch();
                branch.setName(faker.company().name() + " Branch");
                
                // Generate unique branch code
                String branchCode = generateUniqueBranchCode(provinceAbbrev, i);
                
                branch.setCode(branchCode);
                branch.setProvince(province);
                branch.setIsActive(true);
                branch.setIsDeleted(false);
                
                branches.add(branch);
                usedBranchCodes.add(branchCode);
            }
        }

        // Add extra branches to reach ~100
        while (branches.size() < 100) {
            Province randomProvince = provinces.get(faker.random().nextInt(provinces.size()));
            String provinceAbbrev = getProvinceAbbreviation(randomProvince.getName());
            
            Branch branch = new Branch();
            branch.setName(faker.company().name() + " Extra Branch");
            
            String branchCode = generateUniqueBranchCode(provinceAbbrev + "X", branches.size() + 1);
            
            branch.setCode(branchCode);
            branch.setProvince(randomProvince);
            branch.setIsActive(true);
            branch.setIsDeleted(false);
            
            branches.add(branch);
            usedBranchCodes.add(branchCode);
        }

        return branches;
    }

    private String generateUniqueBranchCode(String prefix, int index) {
        String branchCode;
        int attempt = 0;
        do {
            branchCode = prefix + "-" + String.format("%03d", index);
            if (attempt > 0) {
                branchCode = prefix + "-" + String.format("%03d", index) + "-" + attempt;
            }
            attempt++;
        } while (usedBranchCodes.contains(branchCode) && attempt < 100);
        return branchCode;
    }

    private String getProvinceAbbreviation(String provinceName) {
        Map<String, String> abbreviations = new HashMap<>();
        abbreviations.put("Aceh", "ACE");
        abbreviations.put("Sumatera Utara", "SUMUT");
        abbreviations.put("Sumatera Barat", "SUMBAR");
        abbreviations.put("Riau", "RIAU");
        abbreviations.put("Jambi", "JAM");
        abbreviations.put("Sumatera Selatan", "SUMSEL");
        abbreviations.put("Bengkulu", "BEN");
        abbreviations.put("Lampung", "LAMP");
        abbreviations.put("Kepulauan Bangka Belitung", "BABEL");
        abbreviations.put("Kepulauan Riau", "KEPRI");
        abbreviations.put("DKI Jakarta", "JKT");
        abbreviations.put("Jawa Barat", "JABAR");
        abbreviations.put("Jawa Tengah", "JATENG");
        abbreviations.put("DI Yogyakarta", "JOGJA");
        abbreviations.put("Jawa Timur", "JATIM");
        abbreviations.put("Banten", "BANT");
        abbreviations.put("Bali", "BALI");
        abbreviations.put("Nusa Tenggara Barat", "NTB");
        abbreviations.put("Nusa Tenggara Timur", "NTT");
        abbreviations.put("Kalimantan Barat", "KALBAR");
        abbreviations.put("Kalimantan Tengah", "KALTENG");
        abbreviations.put("Kalimantan Selatan", "KALSEL");
        abbreviations.put("Kalimantan Timur", "KALTIM");
        abbreviations.put("Kalimantan Utara", "KALTARA");
        abbreviations.put("Sulawesi Utara", "SULUT");
        abbreviations.put("Sulawesi Tengah", "SULTENG");
        abbreviations.put("Sulawesi Selatan", "SULSEL");
        abbreviations.put("Sulawesi Tenggara", "SULTRA");
        abbreviations.put("Gorontalo", "GOR");
        abbreviations.put("Sulawesi Barat", "SULBAR");
        abbreviations.put("Maluku", "MALU");
        abbreviations.put("Maluku Utara", "MALUT");
        abbreviations.put("Papua Barat", "PAPBAR");
        abbreviations.put("Papua", "PAPUA");
        
        return abbreviations.getOrDefault(provinceName, 
               provinceName.substring(0, Math.min(3, provinceName.length())).toUpperCase());
    }

    private void createStoresInBatches(List<Branch> branches) {
        usedStoreCodes.clear();
        int totalStores = 0;
        int batchSize = 500; // Smaller batch size to avoid memory issues
        
        for (Branch branch : branches) {
            int storesPerBranch = faker.random().nextInt(180, 220);
            List<Store> storesBatch = new ArrayList<>();
            
            for (int i = 1; i <= storesPerBranch; i++) {
                Store store = createStore(branch, i);
                if (store != null) {
                    storesBatch.add(store);
                    totalStores++;
                    
                    // Save in batches
                    if (storesBatch.size() >= batchSize) {
                        storeRepository.saveAll(storesBatch);
                        storesBatch.clear();
                        System.out.println("Saved " + totalStores + " stores so far...");
                    }
                }
            }
            
            // Save remaining stores in the batch
            if (!storesBatch.isEmpty()) {
                storeRepository.saveAll(storesBatch);
                System.out.println("Saved " + totalStores + " stores so far...");
            }
        }
        
        System.out.println("Total stores created: " + totalStores);
        
        // Create whitelist stores
        createWhitelistStores();
    }

    private Store createStore(Branch branch, int index) {
        Store store = new Store();
        store.setName(faker.company().name() + " Store");
        
        String storeCode = generateUniqueStoreCode(branch.getCode(), index);
        if (storeCode == null) return null; // Skip if couldn't generate unique code
        
        store.setCode(storeCode);
        store.setAddress(faker.address().fullAddress());
        store.setBranch(branch);
        store.setIsActive(true);
        store.setIsDeleted(false);
        
        usedStoreCodes.add(storeCode);
        return store;
    }

    private String generateUniqueStoreCode(String branchCode, int index) {
        String storeCode;
        int attempt = 0;
        do {
            storeCode = branchCode + "-STR" + String.format("%04d", index);
            if (attempt > 0) {
                storeCode = branchCode + "-STR" + String.format("%04d", index) + "-" + attempt;
            }
            attempt++;
            if (attempt >= 100) {
                System.err.println("Could not generate unique store code for branch: " + branchCode + ", index: " + index);
                return null;
            }
        } while (usedStoreCodes.contains(storeCode));
        return storeCode;
    }

    private void createWhitelistStores() {
        List<Store> allStores = storeRepository.findAll();
        List<WhitelistStore> whitelistStores = new ArrayList<>();
        
        // Select 5% of stores to be whitelisted
        int whitelistCount = (int) (allStores.size() * 0.05);
        Set<Long> selectedStoreIds = new HashSet<>();
        
        Random random = new Random();
        while (selectedStoreIds.size() < whitelistCount && selectedStoreIds.size() < allStores.size()) {
            Store randomStore = allStores.get(random.nextInt(allStores.size()));
            if (!selectedStoreIds.contains(randomStore.getId())) {
                WhitelistStore whitelistStore = new WhitelistStore();
                whitelistStore.setStore(randomStore);
                whitelistStore.setIsActive(true);
                whitelistStores.add(whitelistStore);
                selectedStoreIds.add(randomStore.getId());
            }
        }
        
        // Save in batches
        int batchSize = 100;
        for (int i = 0; i < whitelistStores.size(); i += batchSize) {
            int end = Math.min(i + batchSize, whitelistStores.size());
            List<WhitelistStore> batch = whitelistStores.subList(i, end);
            whitelistStoreRepository.saveAll(batch);
        }
        
        System.out.println("Created " + whitelistStores.size() + " whitelist stores");
    }
}