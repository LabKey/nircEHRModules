EHR.model.DataModelManager.registerMetadata('Default', {
    allQueries: {
        Id: {
            xtype: 'ehr-animalIdUpperField',
        },
        'Id/demographics/dam': {
            xtype: 'ehr-animalIdUpperField',
        },
        'Id/demographics/sire': {
            xtype: 'ehr-animalIdUpperField',
        },
        performedby: {
            getInitialValue: function (v, rec) {
                return LABKEY.Security.currentUser.id;
            },
            hidden: false,
            allowBlank: false,
        },
        orderedby: {
            hidden: false,
            allowBlank: false,
            defaultValue: null,
            columnConfig: {
                width: 160
            }
        }
    },
    byQuery: {
        'study.housing': {
            room: {
                allowBlank: true,
                hidden: true
            },
            'cage': {
                allowBlank: false,
                columnConfig: {
                    fixed: true,
                    width: 150
                },
            },
            'enddate': {
                hidden: true
            },
            reason: {
                defaultValue: null,
                allowBlank: false,
                columnConfig: {
                    width: 180
                },
                lookup: {
                    filterArray: [LABKEY.Filter.create('date_disabled', null, LABKEY.Filter.Types.ISBLANK)]
                }
            },
        },
        'study.arrival': {
            initialRoom: {
                allowBlank: true,
                hidden: true
            }
        },
        'study.birth': {
            room: {
                allowBlank: true,
                hidden: true
            }
        },
        'study.exemptions': {
            category: {
                columnConfig: {
                    width: 300
                }
            }
        },
        'study.notes': {
            remark: {
                hidden: false,
                columnConfig: {
                    width: 400
                }
            }
        },
        'study.treatment_order': {
            category: {
                defaultValue: 'Clinical',
                hidden: true
            }
        },
        'study.drug': {
            category: {
                defaultValue: 'Clinical',
                hidden: true
            }
        },
    }
});