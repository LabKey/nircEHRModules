EHR.model.DataModelManager.registerMetadata('DeathNecropsy', {
    allQueries: {
        performedby: {
            allowBlank: false,
            defaultValue: LABKEY.Security.currentUser.displayName,
            lookup: {
                schemaName: 'core',
                queryName: 'users',
                keyColumn: 'DisplayName',
                displayColumn: 'DisplayName',
                columns: 'UserId,DisplayName,FirstName,LastName',
                sort: 'Type,DisplayName'
            },
            editorConfig: {
                anyMatch: true,
                listConfig: {
                    innerTpl: '{[LABKEY.Utils.encodeHtml(values.DisplayName + (values.LastName ? " (" + values.LastName + (values.FirstName ? ", " + values.FirstName : "") + ")" : ""))]}',
                    getInnerTpl: function(){
                        return this.innerTpl;
                    }
                }
            }
        }
    },
    byQuery: {
        'study.necropsy': {
            performedby: {
                hidden: false,
                defaultValue: LABKEY.Security.currentUser.displayName
            },
            necropsyWeight: {
                label: 'Weight (kg)'
            },
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
            attachmentFile: {
                hidden: true
            },
            category: {
                hidden: true
            },
        },
        'study.grossPathology': {
            date: {
                hidden: true,
                getInitialValue: function(v, rec){
                    const necropsyStore = rec.storeCollection.getClientStoreByName('necropsy');
                    if (necropsyStore?.data?.items.length > 0) {
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
            performedby: {
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
            performedby: {
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