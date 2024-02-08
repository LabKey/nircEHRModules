EHR.model.DataModelManager.registerMetadata('DeathNecropsy', {
    byQuery: {
        'study.deaths': {
            performedBy: {
                label: 'Performed By'
            },
            date: {
                label: 'Death Date'
            }
        },
        'study.necropsy': {
            date: {
                label: 'Exam Date',
                editorConfig: {
                    listeners: {
                        change: function (combo, rec) {
                            const panel = combo.up('ehr-dataentrypanel');
                            if (panel) {
                                const date = panel.down('datefield')?.value;
                                const store = panel.storeCollection.getClientStoreByName('grossPathology');
                                if (store && date) {
                                    store.data.each(function (dateRec) {
                                        dateRec.set('date', rec);
                                    }, this);
                                }

                                const store2 = panel.storeCollection.getClientStoreByName('tissueDisposition');
                                if (store2 && date) {
                                    store2.data.each(function (dateRec) {
                                        dateRec.set('date', rec);
                                    }, this);
                                }
                            }
                        }
                    }
                }
            },
            grossAbnormalities: {
                xtype: 'ehr-remarkfield',
                height: 100,
                editorConfig: {
                    resizeDirections: 's'
                },
                columnConfig: {
                    width: 200
                }
            },
            diagnosis: {
                xtype: 'ehr-remarkfield',
                label: 'Diagnosis',
                resizeDirections: 's',
                height: 100,
                columnConfig: {
                    width: 200
                },
            },
            relevantHistory: {
                xtype: 'ehr-remarkfield',
                label: 'Relevant History',
                resizeDirections: 's',
                height: 100,
                columnConfig: {
                    width: 200,
                }
            },
        },
        'study.weight': {
            weight: {
                allowBlank: true,
                useNull: true,
                editorConfig: {
                    allowNegative: false,
                    decimalPrecision: 4
                }
            },
            date: {
                hidden: true,
                getInitialValue: function(v, rec){
                    const necropsyStore = rec.storeCollection.getClientStoreByName('necropsy');
                    if (necropsyStore) {
                        return necropsyStore.data.items[0].data.date;
                    }
                }
            },
        },
        'study.grossPathology': {
            date: {
                hidden: true,
                getInitialValue: function(v, rec){
                    const necropsyStore = rec.storeCollection.getClientStoreByName('necropsy');
                    if (necropsyStore) {
                        return necropsyStore.data.items[0].data.date;
                    }
                }
            },
            remark: {
                hidden: true,
                showInGrid: false
            },
            project: {
                hidden: true,
                showInGrid: false
            },
            systemExamined: {
                columnConfig: {
                    width: 200
                }
            },
            performedBy: {
                hidden: true,
                showInGrid: false
            },
            appearance: {
                defaultValue: 'Normal',
                lookup: {
                    schemaName: 'ehr_lookups',
                    queryName: 'necropsy_organ_appearance',
                    keyColumn: 'title',
                    displayColumn: 'title',
                    defaultValue: 'Normal'
                }
            }
        },
        'study.tissueDisposition': {
            date: {
                hidden: true,
                getInitialValue: function(v, rec){
                    const necropsyStore = rec.storeCollection.getClientStoreByName('necropsy');
                    if (necropsyStore) {
                        return necropsyStore.data.items[0].data.date;
                    }
                }
            },
            project: {
                hidden: true,
                showInGrid: false
            },
            performedBy: {
                hidden: true,
                showInGrid: false
            },
            necropsyDispositionCode: {
                columnConfig: {
                    fixed: true,
                    width: 200
                },
            }
        }
    }
});