
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
                    columns: 'project,name',
                    filterArray: [
                        LABKEY.Filter.create('isActive', true, LABKEY.Filter.Types.EQUAL),
                    ]
                }
            }
        },
        'study.protocolAssignment': {
            'project': {
              hidden: true
            },
            'protocol': {
                xtype: 'combo',
                nullable: false,
                columnConfig: {
                    fixed: true,
                    width: 150
                },
                lookup: {
                    schemaName: 'ehr',
                    queryName: 'activeProtocols',
                    keyColumn: 'protocol',
                    columns: 'protocol,title'
                },
            }
        }
    }
});