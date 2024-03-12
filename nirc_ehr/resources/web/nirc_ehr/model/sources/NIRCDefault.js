EHR.model.DataModelManager.registerMetadata('Default', {
    allQueries: {
        performedby: {
            allowBlank: true,
            nullable: true,
            lookup: {
                schemaName: 'core',
                queryName: 'users',
                keyColumn: 'DisplayName',
                displayColumn: 'DisplayName',
                columns: 'UserId,DisplayName,FirstName,LastName',
                sort: 'Type,DisplayName'
            },
            defaultValue: LABKEY.Security.currentUser.displayName
        }
    }
});