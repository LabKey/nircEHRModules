EHR.model.DataModelManager.registerMetadata('Assignment', {
    allQueries: {
        endDate: {
            hidden: true
        }
    },
    byQuery: {
        'study.assignment': {
            'project': {
                xtype: 'combo',
                nullable: false,
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
                nullable: false,
                columnConfig: {
                    fixed: true,
                    width: 150
                },
            }
        }
    }
});