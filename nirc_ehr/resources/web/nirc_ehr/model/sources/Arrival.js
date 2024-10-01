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
        'endDate': {
            hidden: true
        }
    },
    byQuery: {
        'study.arrival': {
            'cage': {
                allowBlank: false,
                columnConfig: {
                    fixed: true,
                    width: 200
                },
            },
            'Id/demographics/species': {
                columnConfig: {
                    fixed: true,
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
            'Id/demographics/geographic_origin': {
                allowBlank: false,
                columnConfig: {
                    fixed: true,
                    width: 200
                }
            },
            project: {
                xtype: 'combo',
                columnConfig: {
                    width: 150
                },
                lookup: {
                    schemaName: 'ehr',
                    queryName: 'project',
                    keyColumn: 'project',
                    columns: 'project,name'
                },
                allowBlank: false
            },
            arrivalProtocol: {
                allowBlank: false,
                columnConfig: {
                    width: 200
                }
            },
            performedby: {
                hidden: true,
                showInGrid: false
            },
            sourceFacility: {
                allowBlank: false,
                columnConfig: {
                    fixed: true,
                    width: 150
                },
            },
            acquisitionType: {
                allowBlank: false,
                columnConfig: {
                    fixed: true,
                    width: 150
                },
            },
            arrivalType: {
                allowBlank: false,
                columnConfig: {
                    width: 200
                }
            }
        }
    }
});