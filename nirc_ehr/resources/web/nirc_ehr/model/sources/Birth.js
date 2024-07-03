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
                allowBlank: false
            },
            project: {
                xtype: 'combo',
                allowBlank: false,
                lookup: {
                    schemaName: 'ehr',
                    queryName: 'project',
                    keyColumn: 'project',
                    columns: 'project,name'
                }
            },
            birthProtocol: {
                columnConfig: {
                    width: 200
                },
                allowBlank: false
            },
            'Id/demographics/birth': {
                allowBlank: false
            },
            'Id/demographics/gender': {
                allowBlank: false
            },
            'Id/demographics/species': {
                columnConfig: {
                    fixed: true,
                    width: 200
                },
                allowBlank: false
            },
        }
    }
});