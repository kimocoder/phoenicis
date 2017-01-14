package com.playonlinux.apps;

import com.playonlinux.apps.dto.ApplicationDTO;
import com.playonlinux.apps.dto.CategoryDTO;
import com.playonlinux.apps.dto.ResourceDTO;
import com.playonlinux.apps.dto.ScriptDTO;

import java.util.*;
import java.util.function.Function;

public class TeeApplicationsSource implements ApplicationsSource {
    private final ApplicationsSource leftApplicationSource;
    private final ApplicationsSource rightApplicationSource;

    /**
     * merges fetched applications from two sources
     * If an application is found in both sources, the leftApplicationSource will be used.
     * @param leftApplicationSource
     * @param rightApplicationSource
     */
    protected TeeApplicationsSource(ApplicationsSource leftApplicationSource,
                                    ApplicationsSource rightApplicationSource) {
        this.leftApplicationSource = leftApplicationSource;
        this.rightApplicationSource = rightApplicationSource;
    }

    @Override
    public List<CategoryDTO> fetchInstallableApplications() {
        final Map<String, CategoryDTO> leftCategories = createSortedMap(leftApplicationSource.fetchInstallableApplications(), CategoryDTO::getName);
        final Map<String, CategoryDTO> rightCategories = createSortedMap(rightApplicationSource.fetchInstallableApplications(), CategoryDTO::getName);

        final SortedMap<String, CategoryDTO> mergedCategories = new TreeMap<>(rightCategories);

        for (String categoryName : leftCategories.keySet()) {
            final CategoryDTO category = leftCategories.get(categoryName);

            if (mergedCategories.containsKey(categoryName)) {
                mergedCategories.put(categoryName, mergeCategories(mergedCategories.get(categoryName), category));
            } else {
                mergedCategories.put(categoryName, category);
            }
        }

        return new ArrayList<>(mergedCategories.values());
    }

    private CategoryDTO mergeCategories(CategoryDTO leftCategory, CategoryDTO rightCategory) {
        final Map<String, ApplicationDTO> leftApplications = createSortedMap(leftCategory.getApplications(), ApplicationDTO::getName);
        final Map<String, ApplicationDTO> rightApplications = createSortedMap(rightCategory.getApplications(), ApplicationDTO::getName);

        final SortedMap<String, ApplicationDTO> mergedApps = new TreeMap<>(rightApplications);

        for (String applicationName : leftApplications.keySet()) {
            final ApplicationDTO application = leftApplications.get(applicationName);

            if (mergedApps.containsKey(applicationName)) {
                mergedApps.put(applicationName, mergeApplications(mergedApps.get(applicationName), application));
            } else {
                mergedApps.put(applicationName, application);
            }
        }

        final List<ApplicationDTO> applications = new ArrayList<>(mergedApps.values());
        applications.sort(ApplicationDTO.nameComparator());
        return new CategoryDTO.Builder()
                .withApplications(applications)
                .withType(leftCategory.getType())
                .withIcon(leftCategory.getIcon())
                .withName(leftCategory.getName())
                .build();
    }

    private ApplicationDTO mergeApplications(ApplicationDTO leftApplication,
                                             ApplicationDTO rightApplication) {
        final List<ScriptDTO> scripts = mergeListOfDtos(leftApplication.getScripts(), rightApplication.getScripts(), ScriptDTO::getName, ScriptDTO.nameComparator());
        final List<ResourceDTO> resources = mergeListOfDtos(leftApplication.getResources(), rightApplication.getResources(), ResourceDTO::getName, ResourceDTO.nameComparator());

        final Set<byte[]> mergeMiniatures = new HashSet<>();
        leftApplication.getMiniatures().forEach(mergeMiniatures::add);
        rightApplication.getMiniatures().forEach(mergeMiniatures::add);

        return new ApplicationDTO.Builder()
                .withName(leftApplication.getName())
                .withResources(resources)
                .withScripts(scripts)
                .withDescription(leftApplication.getDescription())
                .withIcon(leftApplication.getIcon())
                .withMiniatures(new ArrayList<>(mergeMiniatures))
                .build();
    }



    private <T> List<T> mergeListOfDtos(List<T> leftList, List<T> rightList, Function<T, String> nameSupplier, Comparator<T> sorter) {
        final Map<String, T> left = createSortedMap(leftList, nameSupplier);
        final Map<String, T> right = createSortedMap(rightList, nameSupplier);

        final SortedMap<String, T> merged = new TreeMap<>(left);

        for (String name: right.keySet()) {
            final T dto = right.get(name);

            if (!merged.containsKey(name)) {
                merged.put(name, dto);
            }
        }

        final List<T> result = new ArrayList<>(merged.values());
        result.sort(sorter);
        return result;
    }

    private <T> Map<String, T> createSortedMap(List<T> dtos, Function<T, String> nameProvider) {
        final SortedMap<String, T> map = new TreeMap<>();
        dtos.forEach(dto -> map.put(nameProvider.apply(dto), dto));
        return map;
    }
}
