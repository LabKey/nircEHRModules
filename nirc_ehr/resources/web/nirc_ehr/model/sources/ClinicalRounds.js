EHR.model.DataModelManager.registerMetadata('ClinicalRounds', {
    allQueries: {
        Id: {
            inheritFromParent: true,
            editable: false,
            hidden: false,
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
                xtype: 'nirc_ehr-animalIdCases',
                inheritFromParent: false,
                editable: true,
                hidden: false,
                columnConfig: {
                    editable: true
                }
            },
            date: {
                id: 'open_date',
                editorConfig: {
                    editable: false,
                    disabled: true,
                    fieldCls: 'form-panel-input-disabled',
                    labelClsExtra: 'form-panel-input-disabled'
                },
                getInitialValue: function(v, rec){
                    return null;
                }
            },
            enddate: {
                id: 'close_date',
                editorConfig: {
                    editable: false,
                    disabled: true,
                    fieldCls: 'form-panel-input-disabled',
                    labelClsExtra: 'form-panel-input-disabled'
                }
            },
            category: {
                getInitialValue: function (v, rec){
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
                    editable: false,
                    disabled: true,
                    fieldCls: 'form-panel-input-disabled',
                    labelClsExtra: 'form-panel-input-disabled'
                }
            },
            problemSubcategory: {
                editorConfig: {
                    editable: false,
                    disabled: true,
                    fieldCls: 'form-panel-input-disabled',
                    labelClsExtra: 'form-panel-input-disabled'
                }
            },
            plan: {
                editorConfig: {
                    editable: false,
                    disabled: true,
                    fieldCls: 'form-panel-input-disabled',
                    labelClsExtra: 'form-panel-input-disabled'
                }
            },
            openRemark: {
                editorConfig: {
                    editable: false,
                    disabled: true,
                    fieldCls: 'form-panel-input-disabled',
                    labelClsExtra: 'form-panel-input-disabled'
                }
            },
            closeRemark: {
                editorConfig: {
                    editable: false,
                    disabled: true,
                    fieldCls: 'form-panel-input-disabled',
                    labelClsExtra: 'form-panel-input-disabled'
                }
            },
            performedby: {
                hidden: false,
                editorConfig: {
                    editable: false,
                    disabled: true,
                    fieldCls: 'form-panel-input-disabled',
                    labelClsExtra: 'form-panel-input-disabled'
                }
            },
            formCasesLink: {
                xtype: 'nirc_ehr-editCases',
                editorConfig: {
                    readonly: true
                }
            },
        }
    }
});