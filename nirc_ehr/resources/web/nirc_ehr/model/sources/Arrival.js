Ext4.onReady(function() {
    // this is to skip Id not found warning during weights entry in Arrival data entry form
    if (EHR.data.DataEntryClientStore) {
        Ext4.override(EHR.data.DataEntryClientStore, {
            getExtraContext: function(){
                return {
                    skipIdNotFoundError: {'form': 'arrival'}
                }
            }
        });
    }
});

EHR.model.DataModelManager.registerMetadata('Arrival', {
    allQueries: {

    },
    byQuery: {
        'study.arrival': {
            'cage': {
                columnConfig: {
                    fixed: true,
                    width: 200
                },
            },
            'Id/demographics/species': {
                columnConfig: {
                    fixed: true,
                    width: 250
                }
            },
            project: {
                hidden: true,
                allowBlank: true
            },
            performedby: {
                hidden: true,
                showInGrid: false
            },
            sourceFacility: {
                columnConfig: {
                    fixed: true,
                    width: 150
                },
            },
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