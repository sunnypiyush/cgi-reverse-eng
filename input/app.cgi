#!/usr/bin/perl
use strict;
use warnings;
use CGI qw(:standard);
use JSON;

# File to store tasks
my $TASKS_FILE = '/Users/piyush.a.yadav/Desktop/Code/AgenticProject/cgi-project/cgi-reverse-eng/input/tasks.json';

# Load tasks from file
sub load_tasks {
    return [] unless -e $TASKS_FILE;
    
    open my $fh, '<', $TASKS_FILE or do {
        warn "Cannot read $TASKS_FILE: $!";
        return [];
    };
    my $json_text = do { local $/; <$fh> };
    close $fh;
    
    return [] unless $json_text && $json_text =~ /\S/;
    
    my $data = eval { decode_json($json_text) };
    if ($@) {
        warn "JSON decode error: $@";
        return [];
    }
    return $data || [];
}

# Save tasks to file
sub save_tasks {
    my ($tasks) = @_;
    
    # Ensure we have an array ref
    $tasks = [] unless ref($tasks) eq 'ARRAY';
    
    my $json = encode_json($tasks);
    
    open my $fh, '>', $TASKS_FILE or do {
        warn "Cannot write to $TASKS_FILE: $!";
        return 0;
    };
    print $fh $json;
    close $fh;
    
    # Verify write
    warn "Saved " . scalar(@$tasks) . " tasks to $TASKS_FILE";
    return 1;
}

# Main application logic
my $app = sub {
    my $env = shift;
    my $q = CGI->new($env);
    
    # Load existing tasks
    my $tasks = load_tasks();
    
    # Handle form submission
    my $action = $q->param('action') || '';
    my $message = '';
    
    if ($action eq 'add') {
        my $task = $q->param('task');
        if ($task) {
            push @$tasks, {
                id => time() . int(rand(1000)),
                text => $task,
                created => scalar localtime()
            };
            if (save_tasks($tasks)) {
                $message = 'Task added and saved successfully!';
            } else {
                $message = 'Task added but failed to save to file.';
            }
        }
    } elsif ($action eq 'delete') {
        my $id = $q->param('id');
        $tasks = [grep { $_->{id} ne $id } @$tasks];
        if (save_tasks($tasks)) {
            $message = 'Task deleted successfully!';
        }
    } elsif ($action eq 'clear') {
        $tasks = [];
        if (save_tasks($tasks)) {
            $message = 'All tasks cleared successfully!';
        }
    } elsif ($action eq 'reload') {
        $tasks = load_tasks();
        $message = 'Tasks reloaded from file. Found ' . scalar(@$tasks) . ' task(s).';
    }
    
    # Generate HTML content
    my $html = generate_html($tasks, $message);
    
    # Return proper PSGI response
    return [
        200,
        ['Content-Type' => 'text/html; charset=UTF-8'],
        [$html]
    ];
};

# Run the app if called directly, otherwise return it for PSGI
unless (caller) {
    # Direct execution - print as CGI
    my $q = CGI->new;
    
    # Load existing tasks
    my $tasks = load_tasks();
    
    # Handle form submission
    my $action = $q->param('action') || '';
    my $message = '';
    
    if ($action eq 'add') {
        my $task = $q->param('task');
        if ($task) {
            push @$tasks, {
                id => time() . int(rand(1000)),
                text => $task,
                created => scalar localtime()
            };
            if (save_tasks($tasks)) {
                $message = 'Task added and saved successfully!';
            } else {
                $message = 'Task added but failed to save to file.';
            }
        }
    } elsif ($action eq 'delete') {
        my $id = $q->param('id');
        $tasks = [grep { $_->{id} ne $id } @$tasks];
        if (save_tasks($tasks)) {
            $message = 'Task deleted successfully!';
        }
    } elsif ($action eq 'clear') {
        $tasks = [];
        if (save_tasks($tasks)) {
            $message = 'All tasks cleared successfully!';
        }
    } elsif ($action eq 'reload') {
        $tasks = load_tasks();
        $message = 'Tasks reloaded from file. Found ' . scalar(@$tasks) . ' task(s).';
    }
    
    print $q->header('text/html');
    print generate_html($tasks, $message);
} else {
    return $app;
}

sub generate_html {
    my ($tasks, $message) = @_;
    $message ||= '';
    
    my $html = <<'HTML';
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Task Management System</title>
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }
        
        body {
            font-family: Arial, sans-serif;
            background-color: #f5f5f5;
            padding: 20px;
        }
        
        .header-bar {
            background-color: #5c4033;
            height: 30px;
            margin-bottom: 20px;
        }
        
        .nav-links {
            text-align: center;
            margin-bottom: 20px;
        }
        
        .nav-links a {
            color: #8b0000;
            text-decoration: underline;
            font-size: 18px;
            margin: 0 10px;
        }
        
        h1 {
            text-align: center;
            font-size: 28px;
            margin-bottom: 30px;
            font-weight: bold;
        }
        
        .container {
            max-width: 1200px;
            margin: 0 auto;
            background-color: white;
        }
        
        .summary-box {
            border: 3px solid #333;
            margin-bottom: 20px;
        }
        
        .summary-header {
            background-color: #ffb3b3;
            padding: 15px;
            border-bottom: 2px solid #333;
        }
        
        .summary-header h2 {
            font-size: 22px;
            margin-bottom: 10px;
        }
        
        .info-grid {
            display: grid;
            grid-template-columns: 1fr 1fr;
            gap: 10px;
        }
        
        .info-item {
            display: flex;
        }
        
        .info-label {
            font-weight: bold;
            margin-right: 10px;
        }
        
        .form-section {
            padding: 20px;
            background-color: #fff;
            border: 2px solid #333;
            margin-bottom: 20px;
        }
        
        .form-section h3 {
            margin-bottom: 15px;
            font-size: 18px;
        }
        
        input[type="text"] {
            width: 70%;
            padding: 8px;
            border: 2px solid #999;
            font-size: 14px;
        }
        
        button {
            padding: 8px 20px;
            background-color: #4a90e2;
            color: white;
            border: 2px solid #333;
            font-weight: bold;
            cursor: pointer;
            margin-left: 10px;
        }
        
        button:hover {
            background-color: #357abd;
        }
        
        button.delete {
            background-color: #d9534f;
            padding: 5px 10px;
            font-size: 12px;
            margin-left: 5px;
        }
        
        button.delete:hover {
            background-color: #c9302c;
        }
        
        button.clear {
            background-color: #f0ad4e;
            margin-top: 10px;
        }
        
        button.clear:hover {
            background-color: #ec971f;
        }
        
        button.reload {
            background-color: #5bc0de;
        }
        
        button.reload:hover {
            background-color: #46b8da;
        }
        
        .message {
            padding: 15px;
            margin-bottom: 20px;
            border: 2px solid #333;
            background-color: #d4edda;
            color: #155724;
            font-weight: bold;
        }
        
        .message.error {
            background-color: #f8d7da;
            color: #721c24;
        }
        
        .data-table {
            width: 100%;
            border-collapse: collapse;
            border: 2px solid #333;
            margin-top: 20px;
        }
        
        .data-table th {
            background-color: #d9d9d9;
            border: 1px solid #333;
            padding: 10px;
            text-align: left;
            font-weight: bold;
        }
        
        .data-table td {
            border: 1px solid #333;
            padding: 10px;
            background-color: white;
        }
        
        .data-table tr:nth-child(even) td {
            background-color: #f9f9f9;
        }
        
        .no-records {
            text-align: center;
            padding: 20px;
            background-color: #fff;
            border: 1px solid #333;
        }
        
        .legend {
            margin-top: 20px;
            padding: 15px;
            background-color: #fff;
            border: 2px solid #333;
        }
        
        .legend-title {
            font-weight: bold;
            margin-bottom: 10px;
        }
        
        .legend-item {
            padding: 5px;
            margin: 5px 0;
        }
        
        .status-active {
            background-color: #90EE90;
        }
        
        .status-pending {
            background-color: #FFE4B5;
        }
        
        .task-actions {
            white-space: nowrap;
        }
    </style>
</head>
<body>
    <div class="header-bar"></div>
    
    <div class="nav-links">
        <a href="#" onclick="return false;">Next Page</a>
        <a href="#" onclick="return false;">Previous Page</a>
    </div>
    
    <h1>Task Management System</h1>
    
    <div class="container">
HTML
    
    # Display message if any
    if ($message) {
        $html .= qq{        <div class="message">$message</div>\n};
    }
    
    $html .= <<'HTML';
        <div class="summary-box">
            <div class="summary-header">
                <h2>System Summary</h2>
                <div class="info-grid">
                    <div class="info-item">
                        <span class="info-label">Application:</span>
                        <span>Task Manager v1.0</span>
                    </div>
                    <div class="info-item">
                        <span class="info-label">Status:</span>
                        <span>Active</span>
                    </div>
                    <div class="info-item">
                        <span class="info-label">Storage:</span>
                        <span>tasks.json</span>
                    </div>
                    <div class="info-item">
                        <span class="info-label">Total Tasks:</span>
                        <span>TBD
</span>
                    </div>
                </div>
            </div>
        </div>
        
        <div class="form-section">
            <h3>Add New Task</h3>
            <form method="POST">
                <input type="hidden" name="action" value="add">
                <input type="text" name="task" placeholder="Enter task description..." required>
                <button type="submit">Add Task</button>
            </form>
        </div>
        
        <div class="form-section">
            <h3>Task Operations</h3>
            <form method="POST" style="display: inline;">
                <input type="hidden" name="action" value="reload">
                <button type="submit" class="reload">Reload Tasks from File</button>
            </form>
            <p style="margin-top: 10px; font-size: 13px; color: #666;">
                Click to refresh and load all tasks from tasks.json file
            </p>
        </div>
        
        <table class="data-table">
            <thead>
                <tr>
                    <th>Task ID</th>
                    <th>Task Description</th>
                    <th>Date Created</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody>
HTML

    # Display tasks
    if (@$tasks) {
        foreach my $task (@$tasks) {
            my $text = $task->{text};
            my $id = $task->{id};
            my $time = $task->{created} || '';
            
            # HTML encode text to prevent XSS
            $text =~ s/&/&amp;/g;
            $text =~ s/</&lt;/g;
            $text =~ s/>/&gt;/g;
            $text =~ s/"/&quot;/g;
            
            $html .= qq{                <tr>
                    <td>$id</td>
                    <td>$text</td>
                    <td>$time</td>
                    <td class="task-actions">
                        <form method="POST" style="display: inline;">
                            <input type="hidden" name="action" value="delete">
                            <input type="hidden" name="id" value="$id">
                            <button type="submit" class="delete">Delete</button>
                        </form>
                    </td>
                </tr>\n};
        }
    } else {
        $html .= qq{                <tr>
                    <td colspan="4" class="no-records">No Records Found</td>
                </tr>\n};
    }

    $html .= <<'HTML';
            </tbody>
        </table>
HTML

    # Add clear all button if there are tasks
    if (@$tasks) {
        $html .= qq{        
        <div class="form-section">
            <form method="POST">
                <input type="hidden" name="action" value="clear">
                <button type="submit" class="clear" onclick="return confirm('Are you sure you want to clear all tasks?')">Clear All Tasks</button>
            </form>
        </div>\n};
    }

    $html .= <<'HTML';
        
        <div class="legend">
            <div class="legend-title">Legend:</div>
            <div class="legend-item status-active">Active - Task is current and being tracked</div>
            <div class="legend-item status-pending">Pending - Task awaiting action</div>
        </div>
    </div>
</body>
</html>
HTML

    return $html;
}