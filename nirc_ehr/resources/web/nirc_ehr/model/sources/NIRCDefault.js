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
            defaultValue: LABKEY.Security.currentUser.id.toString(),
            editorConfig: {
                xtype: 'combo',
                displayField: 'DisplayName',
                valueField: 'UserId',
                forceSelection: true,
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
            // having to add performedby here for housing because it is getting overridden in EHR's default.js.
            performedby: {
                allowBlank: false,
                lookup: {
                    schemaName: 'core',
                    queryName: 'users',
                    keyColumn: 'UserId',
                    displayColumn: 'DisplayName'
                },
                editorConfig: {
                    store: {
                        type: 'labkey-store',
                        schemaName: 'core',

                        // 'performedby' is a text field in the dataset and its lookup to the userid is an int field - this mismatch causes it to disappear
                        // from the display when a value is selected from the dropdown even though the 'userid' value gets saved as a text (this behavior was only seen
                        // in the form panel but not in the grid panel).
                        // casting it as a varchar when loading the store fixes this issue.
                        sql: 'SELECT CAST (UserId AS VARCHAR) AS UserId,DisplayName,FirstName,LastName FROM core.users',
                        autoLoad: true
                    }
                }
            },
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
            },
            performedby: {
                editorConfig: {
                    store: {
                        type: 'labkey-store',
                        schemaName: 'core',
                        sql: 'SELECT CAST (UserId AS VARCHAR) AS UserId,DisplayName,FirstName,LastName FROM core.users',
                        autoLoad: true
                    }
                },
                defaultValue: LABKEY.Security.currentUser.id.toString()
            },
        },
        'study.weight': {
            performedby: {
                editorConfig: {
                    store: {
                        type: 'labkey-store',
                        schemaName: 'core',
                        sql: 'SELECT CAST (UserId AS VARCHAR) AS UserId,DisplayName,FirstName,LastName FROM core.users',
                        autoLoad: true
                    }
                },
                defaultValue: LABKEY.Security.currentUser.id.toString()
            }
        },
        'study.flags': {
            performedby: {
                editorConfig: {
                    store: {
                        type: 'labkey-store',
                        schemaName: 'core',
                        sql: 'SELECT CAST (UserId AS VARCHAR) AS UserId,DisplayName,FirstName,LastName FROM core.users',
                        autoLoad: true
                    }
                },
                defaultValue: LABKEY.Security.currentUser.id.toString()
            }
        },
        'study.treatment_order': {
            category: {
                defaultValue: 'Clinical',
                hidden: true
            },
            performedby: {
                hidden: false,
                allowBlank: false,
                defaultValue: null,
                lookup: {
                    schemaName: 'ehr_lookups',
                    queryName: 'veterinarians',
                    keyColumn: 'UserId',
                    displayColumn: 'DisplayName'
                }
            },
        },
        'study.drug': {
            category: {
                defaultValue: 'Clinical',
                hidden: true
            },
            performedby: {
                hidden: false,
                allowBlank: false,
                defaultValue: null,
                lookup: {
                    schemaName: 'ehr_lookups',
                    queryName: 'veterinarians',
                    keyColumn: 'UserId',
                    displayColumn: 'DisplayName'
                }
            }
        },
    }
});