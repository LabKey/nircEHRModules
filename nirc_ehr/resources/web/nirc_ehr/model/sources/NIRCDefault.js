EHR.model.DataModelManager.registerMetadata('Default', {
    allQueries: {
        Id: {
            xtype: 'ehr-animalIdUpperField',
        },
        'Id/demographics/dam': {
            xtype: 'ehr-animalIdUpperField',
        },
        'Id/demographics/sire': {
            xtype: 'ehr-animalIdUpperField',
        },
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
                columnConfig: {
                    fixed: true,
                    width: 150
                },
            },
            'enddate': {
                hidden: true
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
        },
        'study.exemptions': {
            category: {
                columnConfig: {
                    width: 300
                }
            }
        },
        'study.notes': {
            remark: {
                hidden: false,
                columnConfig: {
                    width: 400
                }
            }
        }
    }
});