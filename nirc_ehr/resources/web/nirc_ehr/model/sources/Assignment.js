EHR.model.DataModelManager.registerMetadata('Assignment', {
    allQueries: {

    },
    byQuery: {
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