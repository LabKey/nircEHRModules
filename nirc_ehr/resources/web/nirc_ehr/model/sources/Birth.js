EHR.model.DataModelManager.registerMetadata('Birth', {
    allQueries: {
        'endDate': {
            hidden: true
        }
    },
    byQuery: {
        'study.birth': {
            'Id/demographics/species': {
                columnConfig: {
                    fixed: true,
                    width: 250
                }
            },
            'cage': {
                columnConfig: {
                    fixed: true,
                    width: 200
                },
            },
            project: {
                xtype: 'combo',
                nullable: false,
                lookup: {
                    schemaName: 'ehr',
                    queryName: 'project',
                    keyColumn: 'project',
                    columns: 'project,name'
                }
            }
        }
    }
});