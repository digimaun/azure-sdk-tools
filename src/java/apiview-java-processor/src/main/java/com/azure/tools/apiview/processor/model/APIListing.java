package com.azure.tools.apiview.processor.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class APIListing {
    @JsonProperty("Navigation")
    private List<ChildItem> navigation;

    @JsonIgnore
    private ChildItem rootNav;

    @JsonProperty("Name")
    private String name;

//    @JsonProperty("Language")
    @JsonIgnore
    private String language;

    // This string is taken from here:
    // https://github.com/Azure/azure-sdk-tools/blob/master/src/dotnet/APIView/APIView/Languages/CodeFileBuilder.cs#L50
    @JsonProperty("VersionString")
    private final String versionString = "18";

    @JsonProperty("Tokens")
    private List<Token> tokens;

    @JsonProperty("Diagnostics")
    private List<Diagnostic> diagnostics;

    @JsonIgnore
    private Map<String, String> knownTypes;

    // a map of package names to a list of types within that package
    @JsonIgnore
    private final Map<String, List<String>> packageNamesToTypesMap;

    @JsonIgnore
    private final Map<String, String> typeToPackageNameMap;

    public APIListing(String reviewName) {
        this.name = reviewName;
        this.diagnostics = new ArrayList<>();
        this.knownTypes = new HashMap<>();
        this.packageNamesToTypesMap = new HashMap<>();
        this.typeToPackageNameMap = new HashMap<>();

        this.navigation = new ArrayList<>();
        this.rootNav = new ChildItem(name, TypeKind.ASSEMBLY);
        this.navigation.add(rootNav);
    }

    public void addChildItem(ChildItem childItem) {
        this.rootNav.addChildItem(childItem);
    }

    public void addChildItem(String packageName, ChildItem childItem) {
        this.rootNav.addChildItem(packageName, childItem);
    }

    public void addDiagnostic(Diagnostic diagnostic) {
        this.diagnostics.add(diagnostic);
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(final String language) {
        this.language = language;
    }

    public List<Token> getTokens() {
        return tokens;
    }

    public void setTokens(List<Token> tokens) {
        this.tokens = tokens;
    }

    @Override
    public String toString() {
        return "APIListing [rootNav = "+rootNav+", Name = "+ name +", Tokens = "+tokens+"]";
    }

    /**
     * Returns a map of type name to unique identifier, used for navigation.
     */
    public Map<String, String> getKnownTypes() {
        return knownTypes;
    }

    public void addPackageTypeMapping(String packageName, String typeName) {
        packageNamesToTypesMap.computeIfAbsent(packageName, name -> new ArrayList<>()).add(typeName);
        typeToPackageNameMap.put(typeName, packageName);
    }

    public Map<String, List<String>> getPackageNamesToTypesMap() {
        return packageNamesToTypesMap;
    }

    public Map<String, String> getTypeToPackageNameMap() {
        return typeToPackageNameMap;
    }
}