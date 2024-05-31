package semantics;

public class Identifier {
    private final String name;
    private final String dataType;
    private final String role;
    private final String scope;

    public Identifier(String name, String dataType, String role, String scope) {
        this.name = name;
        this.dataType = dataType;
        this.role = role;
        this.scope = scope;
    }

    public String getName() {
        return name;
    }

    public String getDataType() {
        return dataType;
    }

    public String getRole() {
        return role;
    }

    public String getScope() {
        return scope;
    }

    @Override
    public String toString() {
        return "Identifier{" +
                "name='" + name + '\'' +
                ", dataType='" + dataType + '\'' +
                ", role='" + role + '\'' +
                ", scope='" + scope + '\'' +
                '}';
    }
}
