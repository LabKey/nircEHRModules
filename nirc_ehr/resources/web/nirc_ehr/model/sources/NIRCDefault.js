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
    },
    byQuery: {
        'study.housing': {
            room: {
                allowBlank: true,
                hidden: true
            },
            'cage': {
                // xtype: 'combo',
                // lookup: {
                //     schemaName: 'ehr_lookups',
                //     queryName: 'cage',
                //     keyColumn: 'location',
                //     displayColumn: 'cage',
                //     columns: 'location,cage'
                // },
                columnConfig: {
                    fixed: true,
                    width: 150
                },
            }
        },
        'study.arrival': {
            initialRoom: {
                allowBlank: true,
                hidden: true
            }
        },
        'study.birth': {
            room: {
                allowBlank: true,
                hidden: true
            }
        }
    }
});