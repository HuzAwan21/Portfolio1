// ===== PORTFOLIO DATA CONFIG =====
// This file contains all your portfolio data. Edit it to add your projects, research, CV, etc.

const portfolioData = {
    // Basic Information
    name: "Huzaifa Ahmad Awan",
    title: "Computer Science Student | AI Enthusiast | Problem Solver",
    bio: "I'm a passionate computer science student at Universität des Saarlandes specializing in Software Engineering, Artificial Intelligence, and Compiler Construction. I have hands-on experience in system programming, machine learning, and big data engineering. Driven by curiosity and a strong desire to build impactful solutions through innovative technology.",
    
    // Profile Image URL (add your photo URL here)
    profileImage: "https://via.placeholder.com/300?text=Huzaifa+Ahmad+Awan",
    
    // CV File URL (upload your CV and add the URL here)
    cvUrl: "file:///c:/Users/awan2/Desktop/Huzaifa_Ka_Samaan/Arbeit/Lebenslauf/lebenslauf2025.pdf",
    
    // Skills
    skills: [
        "C",
        "C++",
        "Java",
        "Python",
        "RISC-V",
        "Machine Learning",
        "Artificial Intelligence",
        "SQL",
        "Django",
        "System Programming",
        "Data Structures & Algorithms",
        "Git"
    ],
    
    // Projects Array - Add your projects here
    projects: [
        {
            id: 1,
            title: "C Compiler Implementation",
            description: "Built a comprehensive compiler for a subset of C programming language",
            category: "ai",
            image: "⚙️",
            tags: ["C++", "Compiler Construction", "Algorithms"],
            link: "#",
            details: "Developed a simple C compiler from scratch in C++. Implemented complete compiler pipeline including:\n\n• Lexer: Tokenization and lexical analysis\n• Parser: Syntax analysis and AST construction\n• Semantic Analysis: Type checking and symbol resolution\n• Code Generation: Producing executable machine code\n\nThis project demonstrates deep understanding of compiler design patterns, grammar processing, and code generation techniques. Part of the Compiler Construction course at Universität des Saarlandes."
        },
        {
            id: 2,
            title: "AI Planning with Heuristics",
            description: "Implemented advanced heuristic algorithms for classical planning problems",
            category: "ai",
            image: "🤖",
            tags: ["Artificial Intelligence", "Python", "Algorithms"],
            link: "#",
            details: "Implemented Pattern Database (PDB) and Cliques heuristics for solving classical planning tasks. Key accomplishments:\n\n• Pattern Database Heuristic: Precomputed abstractions for optimal distance estimation\n• Cliques Heuristic: Novel approach for improved plan quality\n• Regression Search & Dijkstra's Algorithm: Optimal distance function calculation\n• Efficient state space exploration and heuristic evaluation\n\nThis project showcases expertise in AI algorithms, optimization techniques, and problem-solving approaches used in artificial intelligence planning systems."
        },
        {
            id: 3,
            title: "RISC-V System Programming",
            description: "Low-level system programming with interrupts, exceptions, and process scheduling",
            category: "other",
            image: "💻",
            tags: ["RISC-V", "Assembly", "System Architecture"],
            link: "#",
            details: "Developed advanced RISC-V system programs in Assembly demonstrating low-level system understanding:\n\n• System Calls: Implementing privileged operations and OS interface\n• Interrupt & Exception Handling: Proper control flow management\n• Process Control Blocks (PCB): Data structure design for process management\n• Round-Robin Scheduling: Fair time-slice based scheduling for up to 8 processes\n• STCF (Shortest Time to Completion First): Optimal CPU scheduling algorithm\n• Memory-mapped I/O: Device interaction through memory addresses\n• Control and Status Registers (CSRs): Processor state management\n\nDemonstrates mastery of system-level programming and computer architecture principles."
        },
        {
            id: 4,
            title: "Fame Profiles Social Network Integration",
            description: "Big Data analysis and integration of simulated fame profiles in social networks",
            category: "ai",
            image: "📊",
            tags: ["Python", "Django", "Big Data", "Machine Learning"],
            link: "#",
            details: "Extended Django-based social network application with sophisticated big data features:\n\n• Big Data Methods: Analysis and evaluation of user activities at scale\n• API Development: Built APIs for controlling negative/positive contributions\n• Dynamic Community Management: Automated community membership changes\n• Fame Profile Simulation: Generated and integrated simulated user profiles\n• Data Analysis: Behavioral pattern recognition and trend analysis\n• Django Backend: Scalable web application architecture\n\nThis project demonstrates full-stack development skills combining web frameworks with big data analysis techniques and machine learning approaches."
        }
    ],
    
    // Research Array - Add your research here
    research: [
        {
            id: 1,
            title: "Research Paper 1",
            date: "2024",
            description: "Description of your research paper",
            url: "#",
            abstractShort: "Brief abstract of the research"
        }
        // Add more research items
    ],
    
    // Presentations Array - Add your presentations here
    presentations: [
        {
            id: 1,
            title: "Presentation 1",
            date: "January 2024",
            event: "Tech Conference 2024",
            description: "Description of your presentation topic",
            url: "#",
            icon: "📊"
        }
        // Add more presentations
    ],
    
    // Gallery Images - Add your gallery items here
    gallery: [
        {
            id: 1,
            title: "Gallery Image 1",
            icon: "🖼️",
            url: "#"
        }
        // Add more gallery items
    ],
    
    // Experience Timeline
    experience: [
        {
            id: 1,
            position: "Working Student (Werkstudent)",
            company: "ZF Friedrichhafen AG",
            duration: "Apr. 2025 - Jun. 2025",
            description: "Supporting assembly and production of transmission units with focus on efficiency and precision. Current internship role.",
            highlights: [
                "Assembly and production of complex transmission units according to technical specifications",
                "Functional and visual quality control to ensure assembly correctness",
                "Flexible work requiring teamwork, adaptability, and quick learning",
                "Precision-oriented work in a high-tech automotive production environment"
            ]
        },
        {
            id: 2,
            position: "Working Student (Summer Job)",
            company: "ZF Friedrichhafen AG",
            duration: "Jul. 2023 - Aug. 2023",
            description: "Summer internship supporting production and assembly operations in automotive transmission manufacturing.",
            highlights: [
                "Production support in transmission unit assembly",
                "Quality assurance and control procedures",
                "Team collaboration in industrial manufacturing environment",
                "Practical experience in automotive industry operations"
            ]
        },
        {
            id: 3,
            position: "Retail Assistant (Aushilfe)",
            company: "Netto Marken-Discount",
            duration: "Jul. 2022 - Mar. 2023",
            description: "Retail assistant in food department with responsibilities including inventory management, customer service, and cashier operations.",
            highlights: [
                "Warehouse receiving and inventory management",
                "Product presentation and pricing",
                "Cash register operations and accurate reconciliation",
                "Customer service with focus on positive shopping experience",
                "Attention to detail and accuracy in all transactions"
            ]
        },
        {
            id: 4,
            position: "Intern - Sales & Decoration",
            company: "Anson's Herrenhaus KG",
            duration: "Nov. 2019",
            description: "Student internship focused on sales support and product presentation.",
            highlights: [
                "Customer consultation and advice",
                "Product presentation and merchandising",
                "Initial business operations exposure",
                "Team collaboration in retail environment",
                "Customer-focused service delivery"
            ]
        }
    ],
    
    // Education Timeline
    education: [
        {
            id: 1,
            degree: "B.Sc. Computer Science (Informatik)",
            field: "Computer Science",
            institution: "Universität des Saarlandes",
            year: "Oct. 2023 - Present",
            details: "Focus on Software Engineering, Artificial Intelligence, and Compiler Construction. Relevant coursework: Algorithms & Data Structures, Software Engineering, Compiler Construction, Machine Learning, Artificial Intelligence, System Architecture, Big Data Engineering, Theoretical Computer Science."
        },
        {
            id: 2,
            degree: "Abitur",
            field: "General Education",
            institution: "Albert-Einstein-Gymnasium",
            year: "Aug. 2017 - Jun. 2023",
            details: "Advanced Courses: Mathematics, Physics. Foundation Courses: English, Political Science, Visual Arts. Strong foundation in STEM subjects."
        }
    ],
    
    // Contact Information
    contact: {
        email: "awan2104.huzaifaahmad@gmail.com",
        phone: "+49 1522 9947701",
        location: "Saarbrücken, Germany",
        linkedIn: "https://linkedin.com",
        github: "https://github.com/HuzAwan21",
        twitter: "#"
    },
    
    // Social Media Links
    social: [
        { name: "GitHub", url: "https://github.com/HuzAwan21", icon: "fab fa-github" },
        { name: "Email", url: "mailto:awan2104.huzaifaahmad@gmail.com", icon: "fas fa-envelope" }
        // Add more social links as you create them
    ]
};

// Export for use in script.js
if (typeof module !== 'undefined' && module.exports) {
    module.exports = portfolioData;
}
