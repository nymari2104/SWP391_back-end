package com.example.demo.configuration; // Đảm bảo package này đúng với vị trí file

import org.hibernate.boot.model.TypeContributions;
import org.hibernate.dialect.PostgreSQLDialect;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.type.SqlTypes;
import org.hibernate.type.descriptor.sql.internal.DdlTypeImpl;

public class CustomPostgreSQLDialect extends PostgreSQLDialect {

    @Override
    public void contributeTypes(TypeContributions typeContributions, ServiceRegistry serviceRegistry) {
        // Luôn gọi super() để giữ lại các định nghĩa mặc định của PostgreSQL
        super.contributeTypes(typeContributions, serviceRegistry);

        // Lấy DDL type registry từ TypeConfiguration
        var ddlTypeRegistry = typeContributions.getTypeConfiguration().getDdlTypeRegistry();

        // 1. Ánh xạ NVARCHAR sang TEXT (Giải quyết vấn đề tương thích với SQL Server)
        ddlTypeRegistry.addDescriptor(
                new DdlTypeImpl(SqlTypes.NVARCHAR, "text", this)
        );

        // 2. Ánh xạ VARCHAR sang TEXT
        ddlTypeRegistry.addDescriptor(
                new DdlTypeImpl(SqlTypes.VARCHAR, "text", this)
        );

        // 3. Ánh xạ NCHAR sang CHAR
        ddlTypeRegistry.addDescriptor(
                new DdlTypeImpl(SqlTypes.NCHAR, "char", this)
        );
    }
}