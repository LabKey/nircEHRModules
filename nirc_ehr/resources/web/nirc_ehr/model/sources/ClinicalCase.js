
EHR.model.DataModelManager.registerMetadata('ClinicalCase', {
    allQueries: {
        Id: {
            inheritFromParent: true,
            editable: false,
            hidden: true,
            columnConfig: {
                editable: false
            }
        },
        date: {
            editable: true,
            hidden: false,
            columnConfig: {
                editable: true
            }
        },
        caseid: {
            inheritFromParent: true,
            editable: false,
            hidden: true,
            columnConfig: {
                editable: false
            }
        }
    },
    byQuery: {
        'study.cases': {
            Id: {
                inheritFromParent: false,
                editable: true,
                hidden: false,
                columnConfig: {
                    editable: true
                }
            },
            category: {
                getInitialValue: function(v, rec){
                    return 'Clinical'
                },
                editable: false,
                hidden: true,
                columnConfig: {
                    editable: false
                }
            },
            problemCategory: {
                editorConfig: {
                    listeners: {
                        select: function(field, recs){
                            if (!recs || recs.length !== 1)
                                return;

                            var record = EHR.DataEntryUtils.getBoundRecord(field);
                            if (record){
                                var rec = recs[0];
                                var meta = record.store.model.prototype.fields.get('problemSubcategory');
                                var storeId = LABKEY.ext4.Util.getLookupStoreId(meta);
                                var store = Ext4.StoreMgr.get(storeId);
                                if (store){
                                    store.filterArray = [LABKEY.Filter.create('category', rec.get('value'))];
                                    store.load();
                                }
                            }
                        }
                    }
                },
            }
        }
    }
});