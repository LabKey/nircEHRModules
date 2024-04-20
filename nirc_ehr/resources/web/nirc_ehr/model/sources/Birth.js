Ext4.onReady(function() {
    // this is to skip Id not found warning during weights entry in Birth data entry form
    if (EHR.data.DataEntryClientStore) {
        Ext4.override(EHR.data.DataEntryClientStore, {
            getExtraContext: function(){
                return {
                    skipIdNotFoundError: {'form': 'birth'}
                }
            }
        });
    }
});

EHR.model.DataModelManager.registerMetadata('Birth', {
    allQueries: {

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