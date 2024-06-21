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
            allowBlank: true,
            nullable: true,
            editorConfig: {
                xtype: 'combo',
                displayField: 'DisplayName',
                valueField: 'UserId',
                forceSelection: true,
                store: {
                    type: 'labkey-store',
                    schemaName: 'core',

                    // 'performedby' is a text field in the dataset and its lookup to the userid is an int field - this mismatch causes it to disappear
                    // from the display when a value is selected from the dropdown even though the 'userid' value gets saved as a text (this behavior was only seen
                    // in the form panel but not in the grid panel).
                    // casting it as a varchar when loading the store fixes this issue.
                    sql: 'SELECT CAST (UserId AS VARCHAR) AS UserId,DisplayName,FirstName,LastName FROM core.users',
                    autoLoad: true
                },
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
        'study.housing': {
            room: {
                allowBlank: true,
                hidden: true
            },
            'cage': {
                columnConfig: {
                    fixed: true,
                    width: 150
                },
            },
            'enddate': {
                hidden: true
            }
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
        }
    }
});