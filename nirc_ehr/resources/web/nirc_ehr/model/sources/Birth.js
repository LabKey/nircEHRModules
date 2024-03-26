EHR.model.DataModelManager.registerMetadata('Birth', {
    allQueries: {

    },
    byQuery: {
        'study.birth': {
            'Id/demographics/dam': {
                xtype: 'ehr-animalfield'
            },
            'Id/demographics/sire': {
                xtype: 'ehr-animalfield'
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
        'study.assignment': {
            'project': {
                xtype: 'combo',
                lookup: {
                    schemaName: 'ehr',
                    queryName: 'project',
                    keyColumn: 'project',
                    columns: 'project,name'
                }
            }
        },
        'study.protocolAssignment': {
            'protocol': {
                xtype: 'combo',
                columnConfig: {
                    fixed: true,
                    width: 150
                },
            }
        }
    }
});